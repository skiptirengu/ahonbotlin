package bo.mdia.ahonbotlin

import discord4j.core.GatewayDiscordClient
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class AhonbotlinApplication

suspend fun main(args: Array<String>) {
  runApplication<AhonbotlinApplication>(*args).let {
    val client = it.getBean(GatewayDiscordClient::class.java)

    client.onDisconnect().awaitSingleOrNull()
  }
}
