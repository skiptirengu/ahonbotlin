package bo.mdia.ahonbotlin.config

import discord4j.core.DiscordClientBuilder
import discord4j.core.GatewayDiscordClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordConfig {
  @Value("\${discord.token}") lateinit var token: String

  @Bean
  fun gatewayDiscordClient(): GatewayDiscordClient =
    DiscordClientBuilder.create(token).build().gateway().login().block()!!

  @Bean fun discordRestClient(gatewayDiscordClient: GatewayDiscordClient) = gatewayDiscordClient.rest()
}
