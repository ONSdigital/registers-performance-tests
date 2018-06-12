package uk.gov.ons.gatling.conf

import com.typesafe.config.{ConfigFactory, ConfigParseOptions}
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import io.github.lukehutch.fastclasspathscanner.matchprocessor.FileMatchProcessor


object ConfigLoader {
  private lazy val PATH_PREFIX: String = "uk/gov/ons/gatling/conf/"
  private val configParseOptions: ConfigParseOptions = ConfigParseOptions.defaults().setAllowMissing(false)

  private lazy val apiSpecificConfigFileName: String = System.getProperty("configFileName").replace(".conf", "")
  private lazy val apiSpecificConfig = ConfigFactory.parseResources(PATH_PREFIX + apiSpecificConfigFileName + ".conf", configParseOptions)
  private lazy val defaultConfig = ConfigFactory.parseResources(PATH_PREFIX + "default.conf", configParseOptions)

  private lazy val config = ConfigFactory.systemProperties()
    .withFallback(apiSpecificConfig)
    .withFallback(defaultConfig)
    .resolve()

  def apply(configurationKey: String): String = config.getString(configurationKey)

}

object DebugHelper {
  private def troubleShootConfigFactory = {
    println("***********************")
    val fileMatchProcessor: FileMatchProcessor = (relativePath: String, _, _) => {
      println(relativePath)
    }
    new FastClasspathScanner("uk.gov.ons")
      .matchFilenamePattern(".*\\.conf", fileMatchProcessor)
      .scan()
    println("***********************")
  }
}