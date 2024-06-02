package com.github.badpop.hildirwelcome.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permissions {
  RELOAD("hildir-welcome.reload");

  private final String value;
}
