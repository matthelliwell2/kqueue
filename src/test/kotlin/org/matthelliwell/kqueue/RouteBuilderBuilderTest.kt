package org.matthelliwell.kqueue

import io.kotlintest.matchers.be
import io.kotlintest.specs.StringSpec
import org.matthelliwell.kqueue.builder.route

// TODO LIFO queue
// TODO Rules to generating, consuming and producing entities
class RouteBuilderBuilderTest : StringSpec() {
    init {
        "should create simple route" {
            val route = route("r1") {
                source("p1") {
                    queue("q1") {
                        sink("s1")
                    }
                }
            }.build()

            route.id shouldBe "r1"
            val sources = route.sources
            sources.size shouldBe 1

            val queues = sources[0].consumers
            queues.size shouldBe 1

            val queue = queues[0]

            queue should be a Queue::class
            if (queue is Queue) {
                queue.id shouldBe "q1"

                val sinks = queue.consumers
                sinks.size shouldBe 1

                val sink = sinks[0]
                sink should be a Sink::class
                if (sink is Sink) {
                    sink.id shouldBe "s1"
                }
            }
        }

        "should re-use queue under a source" {
            val route = route("r1") {
                source("p1") {
                    queue("q1") {
                        sink("s1")
                    }
                }
                source("s2") {
                    queue("q1")
                }
            }.build()

            val q1 = route.sources[0].consumers[0]
            val otherQ1 = route.sources[1].consumers[0]

            q1 should beTheSameInstanceAs(otherQ1)
        }

        "should re-use queue under a queue" {
            val route = route("r1") {
                source("p1") {
                    queue("q1") {
                        queue("q2") {
                            sink("s1")
                        }
                    }
                    queue("q3") {
                        queue("q2")
                    }
                }
            }.build()

            val q2 = route.sources[0].consumers[0].consumers[0]
            val otherQ2 = route.sources[0].consumers[1].consumers[0]

            q2 should beTheSameInstanceAs(otherQ2)
        }


        "should re-use sink" {
            val route = route("r1") {
                source("p1") {
                    queue("q1") {
                        sink("s1")
                    }
                    queue("q2") {
                        sink("s1")
                    }
                }
            }.build()

            val s1 = route.sources[0].consumers[0].consumers[0]
            val otherS1 = route.sources[0].consumers[1].consumers[0]

            s1 should beTheSameInstanceAs(otherS1)
        }

        "should throw exception if can't find queue to re-use" {
            shouldThrow<java.lang.Exception> {
                route("r1") {
                    source("p1") {
                        queue("q1") {
                            queue("q2") {
                                sink("s1")
                            }
                        }
                        queue("q3") {
                            queue("xxx")
                        }
                    }
                }
            }
        }

        "should throw exception if try and add sink and queue to a queue" {
            shouldThrow<java.lang.Exception> {
                route("r1") {
                    source("p1") {
                        queue("q1") {
                            queue("q2") {
                                sink("s1")
                            }
                            sink("s2")
                        }
                    }
                }
            }
        }

        "should throw exception if try and create queue with same id" {
            shouldThrow<java.lang.Exception> {
                route("r1") {
                    source("s1") {
                        queue("q1") {
                            queue("q1") {
                                sink("s1")
                            }
                        }
                    }
                }
            }
        }

        "should throw exception if try and create source with same id" {
            shouldThrow<java.lang.Exception> {
                route("r1") {
                    source("s1") {
                        queue("q1") {
                            sink("s1")
                        }
                    }
                    source("s1") {
                        queue("q2") {
                            sink("s2")
                        }
                    }
                }
            }
        }

        "should print pretty string" {
            val prettyString = route("r1") {
                source("p1") {
                    queue("q1") {
                        queue("q2") {
                            sink("s1")
                        }
                    }
                    queue("q3") {
                        queue("q2")
                    }
                }
            }.build().toString()

            prettyString shouldBe "Route(r1)\n--Source(p1)\n----Queue(q1)\n------Queue(q2)\n--------Sink(s1)\n----Queue(q3)\n------Queue(q2)\n--------Sink(s1)\n"
        }
    }
}