package io.koosha.foobar.loader.api.service.entitygen.root

import org.slf4j.Logger


interface RootEntityGeneratorService {

    val log: Logger

    fun generate(running: () -> Boolean) {

        log.info("generating...")

        var i = 0

        while (running()) {

            i++
            if (i == Int.MAX_VALUE)
                i = 0
            if (i > 0 && i % 3_000 == 0)
                log.info("still generating...")

            this.tryGenerate()
        }

        log.info("generation finished")
    }

    fun generate(count: Int) {
        require(count > 0) {
            "count must be gte zero, given count=$count"
        }

        for (i in 0 until count)
            this.tryGenerate()
    }

    fun tryGenerate(): Boolean = try {
        if (!this.generate()) {
            log.error("failed")
            false
        }
        else {
            true
        }
    }
    catch (e: Exception) {
        log.error("generate error", e)
        false
    }

    fun generate(): Boolean

}
