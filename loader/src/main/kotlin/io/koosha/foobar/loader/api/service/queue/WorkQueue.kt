package io.koosha.foobar.loader.api.service.queue

import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.LinkedBlockingQueue


@Service
class WorkQueue {

    private val orderRequestLineItemQueue = LinkedBlockingQueue<UUID>()
    private val orderRequestStateQueue = LinkedBlockingQueue<UUID>()


    fun enqueueOrderRequestForLineItem(orderRequestId: UUID) {
        this.orderRequestLineItemQueue.put(orderRequestId)
    }

    fun getOrderRequestForLineItem(): UUID = this.orderRequestLineItemQueue.take()

    fun enqueueOrderRequestForState(orderRequestId: UUID) {
        this.orderRequestStateQueue.put(orderRequestId)
    }

    fun getOrderRequestForState(): UUID = this.orderRequestStateQueue.take()


}
