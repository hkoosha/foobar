package io.koosha.foobar.loader.api.service.entitygen.root

import mu.KLogger


interface RootEntityGeneratorService {

    val log: KLogger


    fun generate(running: () -> Boolean) {

        while (running())
            this.tryGenerate()
    }

    fun generate(count: Int) {
        if (count < 0)
            throw IllegalArgumentException("count must be gte zero, given count=$count")

        for (i in 0 until count)
            this.tryGenerate()
    }

    fun tryGenerate(): Boolean = try {
        this.generate()
    }
    catch (e: Exception) {
        log.error("generate error: ${e.javaClass.name} -> ${e.message}")
        false
    }


    fun generate(): Boolean
}
