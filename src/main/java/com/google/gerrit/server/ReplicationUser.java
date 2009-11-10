// Copyright (C) 2009 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gerrit.server;

import com.google.gerrit.client.reviewdb.AccountGroup;
import com.google.gerrit.client.reviewdb.Change;
import com.google.gerrit.server.config.AuthConfig;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ReplicationUser extends CurrentUser {
  public interface Factory {
    ReplicationUser create(@Assisted Set<AccountGroup.Id> authGroups);
  }

  private Set<AccountGroup.Id> effectiveGroups;

  @Inject
  protected ReplicationUser(AuthConfig authConfig,
      @Assisted Set<AccountGroup.Id> authGroups) {
    super(AccessPath.REPLICATION, authConfig);
    effectiveGroups = new HashSet<AccountGroup.Id>(authGroups);

    if (effectiveGroups.isEmpty()) {
      effectiveGroups.addAll(authConfig.getRegisteredGroups());
    }

    effectiveGroups = Collections.unmodifiableSet(effectiveGroups);
  }

  @Override
  public Set<AccountGroup.Id> getEffectiveGroups() {
    return Collections.unmodifiableSet(effectiveGroups);
  }

  @Override
  public Set<Change.Id> getStarredChanges() {
    return Collections.emptySet();
  }
}
