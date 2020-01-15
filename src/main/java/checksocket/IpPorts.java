package checksocket;

import java.util.List;

public class IpPorts {
    private String ip;
    private List<String> portList;

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
        private IpPorts ipPorts;

        IpPortsBuilder() {
            ipPorts = new IpPorts();
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
