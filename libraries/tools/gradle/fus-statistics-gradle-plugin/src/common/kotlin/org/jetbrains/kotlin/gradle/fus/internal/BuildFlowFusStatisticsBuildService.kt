/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.fus.internal

import org.jetbrains.kotlin.gradle.fus.GradleBuildFusStatisticsService
import org.jetbrains.kotlin.gradle.fus.Metric
import org.jetbrains.kotlin.gradle.fus.UniqueId
import org.jetbrains.kotlin.gradle.fus.internal.BuildFlowFusStatisticsBuildService.BuildFlowFusStatisticsParameters
import java.util.concurrent.ConcurrentLinkedQueue

abstract class BuildFlowFusStatisticsBuildService : GradleBuildFusStatisticsService<BuildFlowFusStatisticsParameters> {
    internal interface BuildFlowFusStatisticsParameters : CommonFusServiceParameters

    private val metrics = ConcurrentLinkedQueue<Metric>()

    override fun close() {
        // since Gradle 8.1 flow action [BuildFinishFlowAction] is used to collect all metrics and write them down in a single file
    }

    /**
     * Returns a list of collected metrics sets.
     *
     *
     * These sets are not going to be merged into one as no aggregation information is present here.
     * Non-thread safe
     */
    fun getAllReportedMetrics(): List<Metric> {
        val reportedMetrics = ArrayList<Metric>()
        parameters.configurationMetrics.orNull?.also {
            reportedMetrics.addAll(it)
        }
        reportedMetrics.addAll(metrics)
        return reportedMetrics
    }

    override fun reportMetric(name: String, value: Boolean, uniqueId: UniqueId) {
        internalReportMetric(name, value, uniqueId)
    }

    override fun reportMetric(name: String, value: String, uniqueId: UniqueId) {
        internalReportMetric(name, value, uniqueId)
    }

    override fun reportMetric(name: String, value: Number, uniqueId: UniqueId) {
        internalReportMetric(name, value, uniqueId)
    }

    private fun internalReportMetric(name: String, value: Any, uniqueId: UniqueId) {
        //all aggregations should be done on IDEA side
        metrics.add(Metric(name, value, uniqueId))
    }
}