package com.nexa.island.core.common.dispatcher

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val nexaDispatcher: NexaDispatchers)

enum class NexaDispatchers {
    Default,
    Io,
    Main
}
