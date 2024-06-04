/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.rhizomedb.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.rhizomedb.TestGeneratorKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/rhizomedb/testData/codegen")
@TestDataPath("$PROJECT_ROOT")
public class RhizomedbJvmIrTextTestGenerated extends AbstractRhizomedbJvmIrTextTest {
  @Test
  public void testAllFilesPresentInCodegen() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/rhizomedb/testData/codegen"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
  }

  @Nested
  @TestMetadata("plugins/rhizomedb/testData/codegen/attributes")
  @TestDataPath("$PROJECT_ROOT")
  public class Attributes {
    @Test
    public void testAllFilesPresentInAttributes() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/rhizomedb/testData/codegen/attributes"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("attributesWithGeneratedEntityType.kt")
    public void testAttributesWithGeneratedEntityType() {
      runTest("plugins/rhizomedb/testData/codegen/attributes/attributesWithGeneratedEntityType.kt");
    }

    @Test
    @TestMetadata("attributesWithGeneratedEntityTypeAndCompanion.kt")
    public void testAttributesWithGeneratedEntityTypeAndCompanion() {
      runTest("plugins/rhizomedb/testData/codegen/attributes/attributesWithGeneratedEntityTypeAndCompanion.kt");
    }

    @Nested
    @TestMetadata("plugins/rhizomedb/testData/codegen/attributes/ref")
    @TestDataPath("$PROJECT_ROOT")
    public class Ref {
      @Test
      public void testAllFilesPresentInRef() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/rhizomedb/testData/codegen/attributes/ref"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("cascadeDeleteByFlag.kt")
      public void testCascadeDeleteByFlag() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/ref/cascadeDeleteByFlag.kt");
      }

      @Test
      @TestMetadata("cascadeDeleteFlag.kt")
      public void testCascadeDeleteFlag() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/ref/cascadeDeleteFlag.kt");
      }

      @Test
      @TestMetadata("manyAttribute.kt")
      public void testManyAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/ref/manyAttribute.kt");
      }

      @Test
      @TestMetadata("optionalAttribute.kt")
      public void testOptionalAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/ref/optionalAttribute.kt");
      }

      @Test
      @TestMetadata("requiredAttribute.kt")
      public void testRequiredAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/ref/requiredAttribute.kt");
      }

      @Test
      @TestMetadata("severalFlags.kt")
      public void testSeveralFlags() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/ref/severalFlags.kt");
      }

      @Test
      @TestMetadata("uniqueFlag.kt")
      public void testUniqueFlag() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/ref/uniqueFlag.kt");
      }
    }

    @Nested
    @TestMetadata("plugins/rhizomedb/testData/codegen/attributes/transient")
    @TestDataPath("$PROJECT_ROOT")
    public class Transient {
      @Test
      public void testAllFilesPresentInTransient() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/rhizomedb/testData/codegen/attributes/transient"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("indexedFlag.kt")
      public void testIndexedFlag() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/transient/indexedFlag.kt");
      }

      @Test
      @TestMetadata("manyAttribute.kt")
      public void testManyAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/transient/manyAttribute.kt");
      }

      @Test
      @TestMetadata("optionalAttribute.kt")
      public void testOptionalAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/transient/optionalAttribute.kt");
      }

      @Test
      @TestMetadata("requiredAttribute.kt")
      public void testRequiredAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/transient/requiredAttribute.kt");
      }

      @Test
      @TestMetadata("senNonMany.kt")
      public void testSenNonMany() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/transient/senNonMany.kt");
      }

      @Test
      @TestMetadata("uniqueFlag.kt")
      public void testUniqueFlag() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/transient/uniqueFlag.kt");
      }
    }

    @Nested
    @TestMetadata("plugins/rhizomedb/testData/codegen/attributes/value")
    @TestDataPath("$PROJECT_ROOT")
    public class Value {
      @Test
      public void testAllFilesPresentInValue() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/rhizomedb/testData/codegen/attributes/value"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("indexedFlag.kt")
      public void testIndexedFlag() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/value/indexedFlag.kt");
      }

      @Test
      @TestMetadata("manyAttribute.kt")
      public void testManyAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/value/manyAttribute.kt");
      }

      @Test
      @TestMetadata("optionalAttribute.kt")
      public void testOptionalAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/value/optionalAttribute.kt");
      }

      @Test
      @TestMetadata("requiredAttribute.kt")
      public void testRequiredAttribute() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/value/requiredAttribute.kt");
      }

      @Test
      @TestMetadata("serializers.kt")
      public void testSerializers() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/value/serializers.kt");
      }

      @Test
      @TestMetadata("setNonMany.kt")
      public void testSetNonMany() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/value/setNonMany.kt");
      }

      @Test
      @TestMetadata("uniqueFlag.kt")
      public void testUniqueFlag() {
        runTest("plugins/rhizomedb/testData/codegen/attributes/value/uniqueFlag.kt");
      }
    }
  }

  @Nested
  @TestMetadata("plugins/rhizomedb/testData/codegen/entityType")
  @TestDataPath("$PROJECT_ROOT")
  public class EntityType {
    @Test
    public void testAllFilesPresentInEntityType() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/rhizomedb/testData/codegen/entityType"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Nested
    @TestMetadata("plugins/rhizomedb/testData/codegen/entityType/withCompanion")
    @TestDataPath("$PROJECT_ROOT")
    public class WithCompanion {
      @Test
      public void testAllFilesPresentInWithCompanion() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/rhizomedb/testData/codegen/entityType/withCompanion"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("generate.kt")
      public void testGenerate() {
        runTest("plugins/rhizomedb/testData/codegen/entityType/withCompanion/generate.kt");
      }
    }

    @Nested
    @TestMetadata("plugins/rhizomedb/testData/codegen/entityType/withoutCompanion")
    @TestDataPath("$PROJECT_ROOT")
    public class WithoutCompanion {
      @Test
      public void testAllFilesPresentInWithoutCompanion() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/rhizomedb/testData/codegen/entityType/withoutCompanion"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("generate.kt")
      public void testGenerate() {
        runTest("plugins/rhizomedb/testData/codegen/entityType/withoutCompanion/generate.kt");
      }
    }
  }
}
