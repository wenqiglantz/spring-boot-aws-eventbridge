# spring-boot-aws-eventbridge

![customer-order-eventbridge.png](customer-order-eventbridge.png)

Event publisher is Customer-Service, which publishes CustomerCreatedEvent to EventBridge. We have defined two targets on the subscriber side, one API Gateway pointing to Order-Service which consumes the CustomerCreatedEvent, the other target is a Lambda function to just print out the event details, merely to demonstrate that we can have multiple targets defined on the subscriber side.

Refer to my Medium article (https://levelup.gitconnected.com/spring-boot-event-driven-programming-with-aws-eventbridge-aa0d8d65f8a8) for step-by-step instructions to use AWS EventBridge for pub/sub between two Spring Boot microservices.
