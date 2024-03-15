package bo.mdia.ahonbotlin.registrar

import bo.mdia.ahonbotlin.command.SlashCommand
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import discord4j.common.JacksonResources
import discord4j.discordjson.json.ApplicationCommandRequest
import discord4j.rest.RestClient
import io.klogging.NoCoLogging
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component

@Component
class GlobalCommandRegistrar(
  @Autowired private val discordRest: RestClient,
  @Autowired private val kommands: List<SlashCommand>,
) : ApplicationRunner, NoCoLogging {
  companion object {
    private const val COMMANDS_PATH = "classpath:commands/**.jsonc"
  }

  override fun run(args: ApplicationArguments?) {
    val matcher = PathMatchingResourcePatternResolver()
    val mapper =
      JacksonResources.createFromObjectMapper(
        JsonMapper.builder(
            JsonFactory.builder()
              .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
              .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
              .build()
          )
          .build()
      )

    logger.info { "Bootstrapping global commands" }

    val commands =
      matcher.getResources(COMMANDS_PATH).map {
        mapper.objectMapper.readValue(it.inputStream, ApplicationCommandRequest::class.java).also {
          logger.info { "Bootstrapping command: ${it.name()}" }
        }
      }

    logger.info { "JSON commands ${commands.size}" }
    logger.info { "Registering ${kommands.size} commands" }

    runBlocking {
      val appId = discordRest.applicationId.awaitLast()
      val applicationCommand =
        discordRest.applicationService
          .getGlobalApplicationCommands(appId)
          .buffer()
          .awaitFirstOrNull()

      logger.info { "Command id ${applicationCommand?.size}" }

      /*
      applicationCommand?.forEach {
        discordRest.applicationService
          .deleteGlobalApplicationCommand(appId, it.id().asLong())
          .awaitFirstOrNull()
      }
      */

      discordRest.applicationService
        .bulkOverwriteGlobalApplicationCommand(appId, kommands.map { it.definition() })
        .awaitFirstOrNull()

      /*
      kommands.forEach {
        discordRest.applicationService
          .createGlobalApplicationCommand(appId, it.definition())
          .awaitFirstOrNull()
      }
      */
    }
  }
}
