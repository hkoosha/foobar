package io.koosha.foobar.maker.api

import org.springframework.boot.ApplicationArguments


interface Command {

    val commandName: String

    fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    )

}
