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

package nem2.guides.examples.restriction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.nem.sdk.api.RepositoryFactory;
import io.nem.sdk.api.RestrictionMosaicRepository;
import io.nem.sdk.infrastructure.vertx.RepositoryFactoryVertxImpl;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.restriction.MosaicAddressRestriction;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

class GettingMosaicAddressRestrictions {

    @Test
    void gettingMosaicAddressRestrictions()
            throws ExecutionException, InterruptedException {
        /* start block 01 */
        // replace with node endpoint
        try (final RepositoryFactory repositoryFactory = new RepositoryFactoryVertxImpl(
                "http://api-xym-harvest-3-01.us-west-2.nemtech.network:3000")) {
            final RestrictionMosaicRepository restrictionRepository = repositoryFactory
                    .createRestrictionMosaicRepository();

            // replace with mosaicId
            final String mosaicIdHex = "634a8ac3fc2b65b3";
            final MosaicId mosaicId = new MosaicId(mosaicIdHex);

            // replace with address
            final String rawAddress = "TBULEA-UG2CZQ-ISUR44-2HWA6U-AKGWIX-HDABJV-IPS4";
            final Address address = Address.createFromRawAddress(rawAddress);

            final List<MosaicAddressRestriction> restrictions;
            restrictions = restrictionRepository
                    .getMosaicAddressRestrictions(mosaicId, Collections.singletonList(address))
                    .toFuture().get();
            final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            System.out.println(gson.toJson(restrictions));
        }
        /* end block 01 */
    }
}
