"use strict";
/*
 *
 * Copyright 2018-present NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
Object.defineProperty(exports, "__esModule", { value: true });
const nem2_sdk_1 = require("nem2-sdk");
/* start block 01 */
// replace with namespace name
const namespaceId = new nem2_sdk_1.NamespaceId('foo');
// replace with node endpoint
const nodeUrl = 'http://api-xym-harvest-3-01.us-west-2.nemtech.network:3000';
const repositoryFactory = new nem2_sdk_1.RepositoryFactoryHttp(nodeUrl);
const namespaceHttp = repositoryFactory.createNamespaceRepository();
namespaceHttp
    .getNamespace(namespaceId)
    .subscribe((namespaceInfo) => console.log(namespaceInfo), (err) => console.error(err));
/* end block 01 */
