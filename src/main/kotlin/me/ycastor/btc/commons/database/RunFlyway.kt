package me.ycastor.btc.commons.database

import io.quarkus.runtime.StartupEvent
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.flywaydb.core.Flyway
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class RunFlyway {
    @ConfigProperty(name = "flyway.migrate")
    var runMigration: Boolean = false

    @ConfigProperty(name = "quarkus.datasource.reactive.url")
    lateinit var datasourceUrl: String

    @ConfigProperty(name = "quarkus.datasource.username")
    lateinit var datasourceUsername: String

    @ConfigProperty(name = "quarkus.datasource.password")
    lateinit var datasourcePassword: String

    fun runFlywayMigration(@Observes event: StartupEvent) {
        if (runMigration) {
            Flyway
                .configure()
                .dataSource("jdbc:$datasourceUrl", datasourceUsername, datasourcePassword)
                .load()
                .migrate()
        }
    }
}
