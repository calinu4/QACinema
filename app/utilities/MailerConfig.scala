package utilities

import com.typesafe.config.Config
import play.api.inject.{ Binding, Module }
import play.api.{ Configuration, Environment }

class MailerConfig extends Module{
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[Config].toInstance(configuration.underlying)
  )
}
