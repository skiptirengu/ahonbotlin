package bo.mdia.ahonbotlin

import java.util.concurrent.CountDownLatch
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class AhonbotlinApplication {
  @Bean fun closeLatch() = CountDownLatch(1)
}

fun main(args: Array<String>) {
  runApplication<AhonbotlinApplication>(*args).let {
    val latch = it.getBean(CountDownLatch::class.java)
    Runtime.getRuntime().addShutdownHook(Thread { latch.countDown() })
    latch.await()
  }
}
