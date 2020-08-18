package checksocket;

import java.util.List;

/** Объектная модель: IP и список портов к нему */
public class IpPorts {
    private String description;
    private String ip;
    private List<String> portList;

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<String> getPortList() {
        return portList;
    }

    public void setPortList(List<String> portList) {
        this.portList = portList;
    }

    public static final class IpPortsBuilder {
        private final IpPorts ipPorts;

        IpPortsBuilder() {
            ipPorts = new IpPorts();
        }

        public IpPortsBuilder withDescription(String description) {
            ipPorts.setDescription(description);
            return this;
        }

        public IpPortsBuilder withIp(String ip) {
            ipPorts.setIp(ip);
            return this;
        }

        public IpPortsBuilder withPortList(List<String> portList) {
            ipPorts.setPortList(portList);
            return this;
        }

        public IpPorts build() {
            return ipPorts;
        }
    }
}