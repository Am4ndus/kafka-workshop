package demo.kafka.kafka;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;

public class ExtractHeaderThenDoSomethingTransformer implements ValueTransformerWithKey<String, String, String> {
    ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public String transform(String readOnlyKey, String value) {
        Headers headers = context.headers();
        var a = 0;
        /* take few descsion based on Header: if you want to filter base on then just return null then chaining another filter operator after transformValues*/
        /* How to get the Header */
        return value;
    }

    @Override
    public void close() {

    }

}
