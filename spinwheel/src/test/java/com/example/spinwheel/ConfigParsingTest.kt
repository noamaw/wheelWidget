package com.example.spinwheel

import com.example.spinwheel.models.RootConfig
import kotlinx.serialization.json.Json
import org.junit.Test

class ConfigParsingTest {

    @Test
    fun parse_config_success() {
        val json = this::class.java
            .getResource("/tapp_widget_config.json")!!
            .readText()

        val parsed = Json.decodeFromString<RootConfig>(json)

        println(parsed)

        assert(parsed.data.isNotEmpty())
    }
}