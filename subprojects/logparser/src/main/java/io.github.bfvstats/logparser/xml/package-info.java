@XmlAccessorType(XmlAccessType.FIELD)
@XmlSchema(
    namespace = BfLog.NAMESPACE,
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
        @XmlNs(prefix = "bf", namespaceURI = BfLog.NAMESPACE)
    }
)
package io.github.bfvstats.logparser.xml;

import javax.xml.bind.annotation.*;


