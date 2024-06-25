/*
 * Copyright 2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

#ifndef CUSTOM_ALLOC_CPP_CUSTOMALLOCCONSTANTS_HPP_
#define CUSTOM_ALLOC_CPP_CUSTOMALLOCCONSTANTS_HPP_

#include <cstddef>
#include <cstdint>

#include "CompilerConstants.hpp"
#include "FixedBlockPage.hpp"
#include "NextFitPage.hpp"
#include "ExtraObjectPage.hpp"

inline constexpr const size_t KiB = 1024;

inline constexpr size_t FIXED_BLOCK_PAGE_SIZE() {
        return kotlin::compiler::fixedBlockPageSize() * KiB;
}

const uint32_t FIXED_BLOCK_PAGE_BUCKET_BIT_LENGTH = 2;
const uint32_t FIXED_BLOCK_PAGE_BUCKET_BIT_MASK = (1 << FIXED_BLOCK_PAGE_BUCKET_BIT_LENGTH) - 1;

inline constexpr const int FIXED_BLOCK_PAGE_MAX_BLOCK_SIZE = 127;

// FIXED_BLOCK_PAGE_MAX_BUCKET should ideally be computed as
// FixedBlockPage::BucketIndex(FIXED_BLOCK_PAGE_MAX_BLOCK_SIZE), but the
// bit-tricks in that function can only be made constexpr with C++20. It is
// instead hand set and validated by FixedBlockPageTest.
inline constexpr const uint32_t FIXED_BLOCK_PAGE_MAX_BUCKET = 23;
inline const size_t FIXED_BLOCK_PAGE_CELL_COUNT =
        ((FIXED_BLOCK_PAGE_SIZE() - sizeof(kotlin::alloc::FixedBlockPage)) / sizeof(kotlin::alloc::FixedBlockCell));

inline constexpr const size_t NEXT_FIT_PAGE_SIZE = (256 * KiB);
inline constexpr const size_t NEXT_FIT_PAGE_CELL_COUNT =
        ((NEXT_FIT_PAGE_SIZE - sizeof(kotlin::alloc::NextFitPage)) / sizeof(kotlin::alloc::Cell));

// NEXT_FIT_PAGE_CELL_COUNT minus one cell for header minus another for the 0-sized dummy block at cells_[0]
inline constexpr const size_t NEXT_FIT_PAGE_MAX_BLOCK_SIZE = (NEXT_FIT_PAGE_CELL_COUNT - 2);

inline constexpr const size_t EXTRA_OBJECT_PAGE_SIZE = 64 * KiB;
inline constexpr const int EXTRA_OBJECT_COUNT =
        (EXTRA_OBJECT_PAGE_SIZE - sizeof(kotlin::alloc::ExtraObjectPage)) / sizeof(kotlin::alloc::ExtraObjectCell);

#endif
