:orphan:

.. post:: 18 Aug, 2018
    :category: Cross-Chain Swaps
    :excerpt: 1
    :nocomments:

########################################################
Atomic cross-chain swap between public and private chain
########################################################

Trade tokens between different blockchains, without using an intermediary party in the process.

**********
Background
**********

Alice and Bob want to exchange **10 alice tokens for 10 bob tokens**.
The problem is that they are not in the same blockchain: alice token is defined in |codename|'s public chain, whereas bob token is only present in a private chain using |codename| technology.

One non-atomic solution could be:

1) Alice sends 10 alice tokens to Bob (private chain)
2) Bob receives the transaction
3) Bob sends 10 bob tokens to Alice (public chain)
4) Alice receives the transaction

However, they do not trust each other that much.
As you may have noticed, Alice could keep Bob tokens if she opts not to perform 3).
Following this guide, we will see how to make this swap possible, trusting on cryptography.

*************
Prerequisites
*************

- Finish the :doc:`getting started section <../../getting-started/setup-workstation>`
- Know how to :doc:`create mosaics <../mosaic/creating-a-mosaic>`

*************************
Method #01: Using the SDK
*************************

Trading tokens directly from one blockchain to the other is not possible, due to the technological differences between them.

In the case of |codename| public and private chain, the same mosaic name could have a different definition and distribution or even not exist.
Between Bitcoin and |codename|, the difference is even more evident, as each blockchain uses an entirely different technology.

Instead of transferring tokens between different chains, the trade will be performed inside each chain.
With cryptography, we will ensure that the token swap occurs atomically.

.. mermaid:: ../../resources/diagrams/cross-chain-swap.mmd
    :caption: Atomic cross-chain swap sequence diagram
    :align: center

For that reason, each actor involved should have at least one account in each blockchain.

.. example-code::

   .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.ts
        :language: typescript
        :start-after:  /* start block 01 */
        :end-before: /* end block 01 */

   .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.js
        :language: javascript
        :start-after:  /* start block 01 */
        :end-before: /* end block 01 */

1. Alice picks a random number, called ``proof``.
Then, applies a **SHA3-256** algorithm to it, obtaining the ``secret``.

.. example-code::

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.ts
        :language: typescript
        :start-after:  /* start block 02 */
        :end-before: /* end block 02 */

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.js
        :language: javascript
        :start-after:  /* start block 02 */
        :end-before: /* end block 02 */

2. Alice creates a **SecretLockTransaction TX1**, including:

* Mosaic: 10 ``00D3378709746FC4`` (alice token)
* Recipient: Bob's address in private chain
* Algorithm: SHA3-256
* Secret:  SHA3-256(proof)
* Duration: 96h
* Network: private chain

.. example-code::

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.ts
        :language: typescript
        :start-after:  /* start block 03 */
        :end-before: /* end block 03 */

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.js
        :language: javascript
        :start-after:  /* start block 03 */
        :end-before: /* end block 03 */

Once announced, this transaction will remain locked until someone discovers the proof that matches the secret.
If after a determined period of time no one proved it, the locked funds will be returned to Alice.

3. Alice signs and announces **TX1** to the **private chain**.

.. example-code::

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.ts
        :language: typescript
        :start-after:  /* start block 04 */
        :end-before: /* end block 04 */

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.js
        :language: javascript
        :start-after:  /* start block 04 */
        :end-before: /* end block 04 */

4. Alice can tell Bob the secret.
Also, he could retrieve it directly from the chain.

5. Bob creates a **SecretLockTransaction TX2**, which contains:

* Mosaic: 10 ``10293DE77C684F71`` (bob token)
* Recipient: Alice's address in public chain
* Algorithm: SHA3-256
* Secret:  SHA3-256(proof)
* Duration: 84h
* Network: public chain

.. example-code::

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.ts
        :language: typescript
        :start-after:  /* start block 05 */
        :end-before: /* end block 05 */

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.js
        :language: javascript
        :start-after:  /* start block 05 */
        :end-before: /* end block 05 */

.. note::  The amount of time in which funds can be unlocked should be a smaller time frame than TX1's. Alice knows the secret, so Bob must be sure he will have some time left after Alice releases the secret.

6. Once signed, Bob announces **TX2** to the **public chain**.

.. example-code::

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.ts
        :language: typescript
        :start-after:  /* start block 06 */
        :end-before: /* end block 06 */

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.js
        :language: javascript
        :start-after:  /* start block 06 */
        :end-before: /* end block 06 */

7. Alice can announce the **SecretProofTransaction TX3** to the public network.
This transaction defines the encrypting algorithm used, the original proof and the secret.
It will unlock TX2 transaction.

.. example-code::

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.ts
        :language: typescript
        :start-after:  /* start block 07 */
        :end-before: /* end block 07 */

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.js
        :language: javascript
        :start-after:  /* start block 07 */
        :end-before: /* end block 07 */

8. The proof is revealed in the public chain.
Bob picks the proof and announces the **SecretProofTransaction TX4** to the **private chain**.

.. example-code::

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.ts
        :language: typescript
        :start-after:  /* start block 08 */
        :end-before: /* end block 08 */

    .. viewsource:: ../../resources/examples/typescript/secretlock/CrossChainSwap.js
        :language: javascript
        :start-after:  /* start block 08 */
        :end-before: /* end block 08 */

Bob receives TX1 funds, and the atomic cross-chain swap concludes.

********************
Is it really atomic?
********************

Consider the following scenarios:

* ✅ Bob does not want to announce TX2: Alice will receive her funds back after 94 hours.

* ✅ Alice does not want to swap tokens by signing TX3: Bob will receive his refund after 84h. Alice will unlock her funds as well after 94 hours.

* ⚠️ Alice signs and announces TX3, receiving Bob's funds: Bob will have time to sign TX4, as TX1 validity is longer than TX2.

The process is atomic, but should be completed with lots of time before the deadlines.
