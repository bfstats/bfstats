
@XmlSchema(
    namespace = BfLog.NAMESPACE,
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
        @XmlNs(prefix = "bf", namespaceURI = BfLog.NAMESPACE)
    }
)
package io.github.bfvstats.logparser.xml;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;


