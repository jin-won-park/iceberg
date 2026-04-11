/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.iceberg.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;

public class TestFileRange {

  @Test
  public void rejectsNullByteBuffer() {
    assertThatThrownBy(() -> new FileRange(null, 0L, 0))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("byteBuffer can't be null");
  }

  @Test
  public void rejectsNegativeLength() {
    CompletableFuture<ByteBuffer> buffer = new CompletableFuture<>();
    assertThatThrownBy(() -> new FileRange(buffer, 0L, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid length: -1");
  }

  @Test
  public void rejectsNegativeOffset() {
    CompletableFuture<ByteBuffer> buffer = new CompletableFuture<>();
    assertThatThrownBy(() -> new FileRange(buffer, -1L, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid offset: -1");
  }

  @Test
  public void acceptsZeroOffsetAndLength() throws Exception {
    CompletableFuture<ByteBuffer> buffer = new CompletableFuture<>();
    FileRange range = new FileRange(buffer, 0L, 0);
    assertThat(range.offset()).isEqualTo(0L);
    assertThat(range.length()).isEqualTo(0);
    assertThat(range.byteBuffer()).isSameAs(buffer);
  }

  @Test
  public void acceptsPositiveOffsetAndLength() throws Exception {
    CompletableFuture<ByteBuffer> buffer = new CompletableFuture<>();
    FileRange range = new FileRange(buffer, 42L, 1024);
    assertThat(range.offset()).isEqualTo(42L);
    assertThat(range.length()).isEqualTo(1024);
  }
}
