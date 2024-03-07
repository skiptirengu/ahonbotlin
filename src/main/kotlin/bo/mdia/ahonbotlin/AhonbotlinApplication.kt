package bo.mdia.ahonbotlin

import discord4j.core.GatewayDiscordClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class AhonbotlinApplication

fun main(args: Array<String>) {
  runApplication<AhonbotlinApplication>(*args).let {
    val client = it.getBean(GatewayDiscordClient::class.java)

    client.onDisconnect().block()
  }
}
