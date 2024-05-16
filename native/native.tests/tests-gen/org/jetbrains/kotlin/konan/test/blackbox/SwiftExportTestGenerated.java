/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.konan.test.blackbox;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateNativeTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("native/native.tests/testData/SwiftExport")
@TestDataPath("$PROJECT_ROOT")
public class SwiftExportTestGenerated extends AbstractNativeSwiftExportTest {
  @Test
  public void testAllFilesPresentInSwiftExport() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("native/native.tests/testData/SwiftExport"), Pattern.compile("^([^_](.+))$"), null, false);
  }

  @Test
  @TestMetadata("properties")
  public void testProperties() {
    runTest("native/native.tests/testData/SwiftExport/properties/");
  }

  @Test
  @TestMetadata("referenceTypes")
  public void testReferenceTypes() {
    runTest("native/native.tests/testData/SwiftExport/referenceTypes/");
  }

  @Test
  @TestMetadata("smokes")
  public void testSmokes() {
    runTest("native/native.tests/testData/SwiftExport/smokes/");
  }
}
