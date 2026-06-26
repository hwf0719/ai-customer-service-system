import os
import re

os.environ["HF_ENDPOINT"] = "https://hf-mirror.com"
os.environ["HUGGINGFACE_HUB_ENDPOINT"] = "https://hf-mirror.com"
os.environ["HF_HOME"] = "./model_cache"
from config import FAQ_FILE, CHUNK_SIZE, CHUNK_OVERLAP, EMBEDDING_MODEL_NAME, RETRIEVER_TOP_K,  \
    CHROMA_PERSIST_DIR

from langchain_community.document_loaders import TextLoader
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_community.vectorstores import Chroma
from langchain_text_splitters import RecursiveCharacterTextSplitter

loader=TextLoader(FAQ_FILE ,encoding="utf-8")
docs=loader.load()
splitter=RecursiveCharacterTextSplitter(
    chunk_size=CHUNK_SIZE,
    chunk_overlap=CHUNK_OVERLAP,
)
chunks=splitter.split_documents(docs)

embeddings=HuggingFaceEmbeddings(model_name=EMBEDDING_MODEL_NAME)
vectorstore=Chroma.from_documents(chunks,embeddings,persist_directory=CHROMA_PERSIST_DIR)

def query(question):
    doc=vectorstore.similarity_search(question,k=RETRIEVER_TOP_K)
    return [d.page_content for d in  doc]

def rebuild_vectorstore():
    global vectorstore
    loader=TextLoader(FAQ_FILE ,encoding="utf-8")
    docs=loader.load()
    chunks=splitter.split_documents(docs)
    vectorstore=Chroma.from_documents(chunks,embeddings,persist_directory=CHROMA_PERSIST_DIR)

def add_faq(question :str,answer :str)-> str:
    texts=f"Q:{question}\nA: {answer}\n\n"
    with open(FAQ_FILE,"a",encoding="utf-8") as f:
        f.write(texts)
    rebuild_vectorstore()
    return "添加成功"

def delete_faq(question :str)-> str:
    with open(FAQ_FILE,"r",encoding="utf-8") as f:
        content=f.read()
    qa_pairs=re.split(r'\n(?=Q:)',content.strip())
    new_pairs=[p for p in qa_pairs if question not in p]
    new_content="\n\n".join(new_pairs)
    with open(FAQ_FILE,"w",encoding="utf-8") as f:
        f.write(new_content)
    rebuild_vectorstore()
    return "删除成功"