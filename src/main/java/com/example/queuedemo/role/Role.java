package com.example.queuedemo.role;/*
 *  Copyright 2020 The MicroMQ Project
 *
 *  The MicroMQ Project licenses this file to you under the Apache License,
 *  version 2.0 (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

import com.example.queuedemo.server.PubRequest;
import com.example.queuedemo.server.PullRequest;
import com.example.queuedemo.transport.TLVData;

public interface Role {
    void start() throws Exception;
    void active();
    void deactive();
    boolean isActive();
    TLVData pub(PubRequest request);
    TLVData pull(PullRequest request);
}
