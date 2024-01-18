@file:Suppress("unused", "DuplicatedCode")

// DO NOT EDIT MANUALLY!
// Generated by generators/tests/org/jetbrains/kotlin/generators/arguments/GenerateCompilerArgumentsCopy.kt
// To regenerate run 'generateCompilerArgumentsCopy' task

package org.jetbrains.kotlin.cli.common.arguments

@OptIn(org.jetbrains.kotlin.utils.IDEAPluginsCompatibilityAPI::class)
fun copyK2NativeCompilerArguments(from: K2NativeCompilerArguments, to: K2NativeCompilerArguments): K2NativeCompilerArguments {
    copyCommonCompilerArguments(from, to)

    to.allocator = from.allocator
    to.autoCacheDir = from.autoCacheDir
    to.autoCacheableFrom = from.autoCacheableFrom?.copyOf()
    to.backendThreads = from.backendThreads
    to.binaryOptions = from.binaryOptions?.copyOf()
    to.bundleId = from.bundleId
    to.cacheDirectories = from.cacheDirectories?.copyOf()
    to.cachedLibraries = from.cachedLibraries?.copyOf()
    to.checkDependencies = from.checkDependencies
    to.checkExternalCalls = from.checkExternalCalls
    to.clangOptions = from.clangOptions?.copyOf()
    to.compileFromBitcode = from.compileFromBitcode
    to.debug = from.debug
    to.debugInfoFormatVersion = from.debugInfoFormatVersion
    to.debugPrefixMap = from.debugPrefixMap?.copyOf()
    to.destroyRuntimeMode = from.destroyRuntimeMode
    to.embedBitcode = from.embedBitcode
    to.embedBitcodeMarker = from.embedBitcodeMarker
    to.emitLazyObjCHeader = from.emitLazyObjCHeader
    to.enableAssertions = from.enableAssertions
    to.exportKDoc = from.exportKDoc
    to.exportedLibraries = from.exportedLibraries?.copyOf()
    to.externalDependencies = from.externalDependencies
    to.fakeOverrideValidator = from.fakeOverrideValidator
    to.filesToCache = from.filesToCache?.copyOf()
    to.frameworkImportHeaders = from.frameworkImportHeaders?.copyOf()
    to.friendModules = from.friendModules
    to.gc = from.gc
    to.generateDebugTrampolineString = from.generateDebugTrampolineString
    to.generateNoExitTestRunner = from.generateNoExitTestRunner
    to.generateTestRunner = from.generateTestRunner
    to.generateWorkerTestRunner = from.generateWorkerTestRunner
    to.headerKlibPath = from.headerKlibPath
    to.includeBinaries = from.includeBinaries?.copyOf()
    to.includes = from.includes?.copyOf()
    to.incrementalCacheDir = from.incrementalCacheDir
    to.konanDataDir = from.konanDataDir
    to.lazyIrForCaches = from.lazyIrForCaches
    to.libraries = from.libraries?.copyOf()
    to.libraryToAddToCache = from.libraryToAddToCache
    to.libraryVersion = from.libraryVersion
    to.lightDebugDeprecated = from.lightDebugDeprecated
    to.lightDebugString = from.lightDebugString
    to.linkerArguments = from.linkerArguments?.copyOf()
    to.listTargets = from.listTargets
    to.llvmVariant = from.llvmVariant
    to.mainPackage = from.mainPackage
    to.makePerFileCache = from.makePerFileCache
    to.manifestFile = from.manifestFile
    to.memoryModel = from.memoryModel
    to.moduleName = from.moduleName
    to.nativeLibraries = from.nativeLibraries?.copyOf()
    to.noObjcGenerics = from.noObjcGenerics
    to.nodefaultlibs = from.nodefaultlibs
    to.noendorsedlibs = from.noendorsedlibs
    to.nomain = from.nomain
    to.nopack = from.nopack
    to.nostdlib = from.nostdlib
    to.omitFrameworkBinary = from.omitFrameworkBinary
    to.optimization = from.optimization
    to.outputName = from.outputName
    to.overrideKonanProperties = from.overrideKonanProperties?.copyOf()
    to.partialLinkageLogLevel = from.partialLinkageLogLevel
    to.partialLinkageMode = from.partialLinkageMode
    to.preLinkCaches = from.preLinkCaches
    to.printBitCode = from.printBitCode
    to.printFiles = from.printFiles
    to.printIr = from.printIr
    to.produce = from.produce
    to.propertyLazyInitialization = from.propertyLazyInitialization
    to.purgeUserLibs = from.purgeUserLibs
    to.refinesPaths = from.refinesPaths?.copyOf()
    to.repositories = from.repositories?.copyOf()
    to.runtimeFile = from.runtimeFile
    to.runtimeLogs = from.runtimeLogs
    to.saveDependenciesPath = from.saveDependenciesPath
    to.saveLlvmIrAfter = from.saveLlvmIrAfter.copyOf()
    to.saveLlvmIrDirectory = from.saveLlvmIrDirectory
    to.serializedDependencies = from.serializedDependencies
    to.shortModuleName = from.shortModuleName
    to.singleLinkerArguments = from.singleLinkerArguments?.copyOf()
    to.staticFramework = from.staticFramework
    to.swiftExportRun = from.swiftExportRun
    to.target = from.target
    to.temporaryFilesDir = from.temporaryFilesDir
    to.testDumpOutputPath = from.testDumpOutputPath
    to.verifyBitCode = from.verifyBitCode
    to.verifyCompiler = from.verifyCompiler
    to.verifyIr = from.verifyIr
    to.workerExceptionHandling = from.workerExceptionHandling

    return to
}
