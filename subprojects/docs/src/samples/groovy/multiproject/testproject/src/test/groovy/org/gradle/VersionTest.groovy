package org.gradle

import org.junit.Test

import static org.junit.Assert.assertEquals

class GroovycVersionTest {
  def groovycVersion

  @Test
  void versionShouldBeCurrent() {
    assertEquals("2.5.4", groovycVersion)
  }
}
