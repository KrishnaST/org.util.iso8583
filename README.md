
# Work in Progress
Please note that this is still **work in progress!**, although quite stable and used in production.

# org.util.iso8583
A Fast and Configurable ISO8583 Encoder and Decoder.

```final ISO8583Message message = new ISO8583Message();
message.put(0, "0200");
message.put(2, "4012888888881881"); 
message.put(3, "000000");
message.put(4, "000000010000");
message.put(11, "112233");
message.put(22, "021");
message.put(25, "05");

final byte[] bytes = EncoderDecoder.encode(NPCIFormat.getInstance(), message);
```

# Documentation
To Do


# License

```
Copyright 2019

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
