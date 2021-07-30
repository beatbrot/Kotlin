/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.test.runners.codegen;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link GenerateNewCompilerTests.kt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/codegen/boxModernJdk")
@TestDataPath("$PROJECT_ROOT")
public class BlackBoxModernJdkCodegenTestGenerated extends AbstractBlackBoxCodegenTest {
    @Test
    public void testAllFilesPresentInBoxModernJdk() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/boxModernJdk"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM, true);
    }

    @Nested
    @Tag("<modernJava>")
    @TestMetadata("compiler/testData/codegen/boxModernJdk/testsWithJava11")
    @TestDataPath("$PROJECT_ROOT")
    public class TestsWithJava11 {
        @Test
        public void testAllFilesPresentInTestsWithJava11() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/boxModernJdk/testsWithJava11"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM, true);
        }

        @Test
        @TestMetadata("concatDynamic.kt")
        public void testConcatDynamic() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamic.kt");
        }

        @Test
        @TestMetadata("concatDynamic200.kt")
        public void testConcatDynamic200() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamic200.kt");
        }

        @Test
        @TestMetadata("concatDynamic200Long.kt")
        public void testConcatDynamic200Long() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamic200Long.kt");
        }

        @Test
        @TestMetadata("concatDynamic201.kt")
        public void testConcatDynamic201() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamic201.kt");
        }

        @Test
        @TestMetadata("concatDynamicIndy200.kt")
        public void testConcatDynamicIndy200() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamicIndy200.kt");
        }

        @Test
        @TestMetadata("concatDynamicIndy200Long.kt")
        public void testConcatDynamicIndy200Long() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamicIndy200Long.kt");
        }

        @Test
        @TestMetadata("concatDynamicIndy201.kt")
        public void testConcatDynamicIndy201() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamicIndy201.kt");
        }

        @Test
        @TestMetadata("concatDynamicInlineClasses.kt")
        public void testConcatDynamicInlineClasses() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamicInlineClasses.kt");
        }

        @Test
        @TestMetadata("concatDynamicSpecialSymbols.kt")
        public void testConcatDynamicSpecialSymbols() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamicSpecialSymbols.kt");
        }

        @Test
        @TestMetadata("concatDynamicWithInline.kt")
        public void testConcatDynamicWithInline() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/concatDynamicWithInline.kt");
        }

        @Test
        @TestMetadata("kt36984.kt")
        public void testKt36984() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/kt36984.kt");
        }

        @Test
        @TestMetadata("varHandle.kt")
        public void testVarHandle() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava11/varHandle.kt");
        }
    }

    @Nested
    @Tag("<modernJava>")
    @TestMetadata("compiler/testData/codegen/boxModernJdk/testsWithJava15")
    @TestDataPath("$PROJECT_ROOT")
    public class TestsWithJava15 {
        @Test
        public void testAllFilesPresentInTestsWithJava15() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/boxModernJdk/testsWithJava15"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM, true);
        }

        @Nested
        @TestMetadata("compiler/testData/codegen/boxModernJdk/testsWithJava15/records")
        @TestDataPath("$PROJECT_ROOT")
        public class Records {
            @Test
            public void testAllFilesPresentInRecords() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/boxModernJdk/testsWithJava15/records"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM, true);
            }

            @Test
            @TestMetadata("bytecodeShapeForJava.kt")
            public void testBytecodeShapeForJava() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/bytecodeShapeForJava.kt");
            }

            @Test
            @TestMetadata("collectionSizeOverrides.kt")
            public void testCollectionSizeOverrides() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/collectionSizeOverrides.kt");
            }

            @Test
            @TestMetadata("dataJvmRecord.kt")
            public void testDataJvmRecord() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/dataJvmRecord.kt");
            }

            @Test
            @TestMetadata("jvmRecordBinary.kt")
            public void testJvmRecordBinary() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/jvmRecordBinary.kt");
            }

            @Test
            @TestMetadata("propertiesOverrides.kt")
            public void testPropertiesOverrides() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/propertiesOverrides.kt");
            }

            @Test
            @TestMetadata("propertiesOverridesAllCompatibilityJvmDefault.kt")
            public void testPropertiesOverridesAllCompatibilityJvmDefault() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/propertiesOverridesAllCompatibilityJvmDefault.kt");
            }

            @Test
            @TestMetadata("propertiesOverridesEnableJvmDefault.kt")
            public void testPropertiesOverridesEnableJvmDefault() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/propertiesOverridesEnableJvmDefault.kt");
            }

            @Test
            @TestMetadata("recordDifferentPropertyOverride.kt")
            public void testRecordDifferentPropertyOverride() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/recordDifferentPropertyOverride.kt");
            }

            @Test
            @TestMetadata("recordDifferentSyntheticProperty.kt")
            public void testRecordDifferentSyntheticProperty() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/recordDifferentSyntheticProperty.kt");
            }

            @Test
            @TestMetadata("recordPropertyAccess.kt")
            public void testRecordPropertyAccess() throws Exception {
                runTest("compiler/testData/codegen/boxModernJdk/testsWithJava15/records/recordPropertyAccess.kt");
            }
        }
    }

    @Nested
    @Tag("<modernJava>")
    @TestMetadata("compiler/testData/codegen/boxModernJdk/testsWithJava17")
    @TestDataPath("$PROJECT_ROOT")
    public class TestsWithJava17 {
        @Test
        public void testAllFilesPresentInTestsWithJava17() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/codegen/boxModernJdk/testsWithJava17"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM, true);
        }

        @Test
        @TestMetadata("compiledJavaSealedClass.kt")
        public void testCompiledJavaSealedClass() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava17/compiledJavaSealedClass.kt");
        }

        @Test
        @TestMetadata("compiledJavaSealedInterface.kt")
        public void testCompiledJavaSealedInterface() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava17/compiledJavaSealedInterface.kt");
        }

        @Test
        @TestMetadata("javaRecordsViaKotlinReflection.kt")
        public void testJavaRecordsViaKotlinReflection() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava17/javaRecordsViaKotlinReflection.kt");
        }

        @Test
        @TestMetadata("javaSealedClass.kt")
        public void testJavaSealedClass() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava17/javaSealedClass.kt");
        }

        @Test
        @TestMetadata("javaSealedInterface.kt")
        public void testJavaSealedInterface() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava17/javaSealedInterface.kt");
        }

        @Test
        @TestMetadata("sealedJavaClassViaJavaReflection.kt")
        public void testSealedJavaClassViaJavaReflection() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava17/sealedJavaClassViaJavaReflection.kt");
        }

        @Test
        @TestMetadata("sealedJavaClassViaKotlinReflection.kt")
        public void testSealedJavaClassViaKotlinReflection() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava17/sealedJavaClassViaKotlinReflection.kt");
        }

        @Test
        @TestMetadata("sealedJavaInterfaceViaKotlinReflection.kt")
        public void testSealedJavaInterfaceViaKotlinReflection() throws Exception {
            runTest("compiler/testData/codegen/boxModernJdk/testsWithJava17/sealedJavaInterfaceViaKotlinReflection.kt");
        }
    }
}
