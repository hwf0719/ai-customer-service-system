import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client/dist/sockjs'

let stompClient = null
const subscribers = new Map()

/**
 * 连接 WebSocket
 */
export function connectWebSocket() {
  if (stompClient && stompClient.connected) {
    return
  }

  stompClient = new Client({
    webSocketFactory: () => new SockJS('/ws'),
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    onConnect: () => {
      console.log('WebSocket 连接成功')
      // 重新订阅之前的主题
      subscribers.forEach((callback, topic) => {
        subscribeToTopic(topic, callback)
      })
    },
    onDisconnect: () => {
      console.log('WebSocket 断开连接')
    },
    onStompError: (frame) => {
      console.error('WebSocket 错误:', frame.headers['message'])
    }
  })

  stompClient.activate()
}

/**
 * 断开 WebSocket 连接
 */
export function disconnectWebSocket() {
  if (stompClient) {
    stompClient.deactivate()
    stompClient = null
  }
}

/**
 * 订阅主题
 */
export function subscribeToTopic(topic, callback) {
  if (stompClient && stompClient.connected) {
    const subscription = stompClient.subscribe(topic, (message) => {
      const data = JSON.parse(message.body)
      callback(data)
    })
    // 保存订阅信息，用于重连时重新订阅
    subscribers.set(topic, callback)
    return subscription
  } else {
    // 如果未连接，先保存订阅信息
    subscribers.set(topic, callback)
  }
}

/**
 * 取消订阅
 */
export function unsubscribeFromTopic(topic) {
  subscribers.delete(topic)
}

/**
 * 发送消息
 */
export function sendMessage(destination, message) {
  if (stompClient && stompClient.connected) {
    stompClient.publish({
      destination: destination,
      body: JSON.stringify(message)
    })
  }
}

/**
 * 获取连接状态
 */
export function isConnected() {
  return stompClient && stompClient.connected
}
