package br.com.empirelands.reflection;

import br.com.empirelands.exception.UnSupportedVersion;
import br.com.empirelands.reflection.v1_8_R1.VersionControl1_8_R1;
import br.com.empirelands.reflection.v1_8_R2.VersionControl1_8_R2;
import br.com.empirelands.reflection.v1_8_R3.VersionControl1_8_R3;

public class ReflectionUtils {

    private String version;
    private NmsVersion nmsVersion;

    /**
     *
     * @param version craftbukkit version
     */
    public ReflectionUtils(String version) throws UnSupportedVersion {
        this.version = version;
        configure();
    }

    private void configure() throws UnSupportedVersion {
        SupportedVersionsAtNow v = SupportedVersionsAtNow.valueOf(this.version);
        switch (v){
            case v1_8_R1:
                this.nmsVersion = VersionControl1_8_R1.getInstance();
                break;
            case v1_8_R2:
                this.nmsVersion = VersionControl1_8_R2.getInstance();
                break;
            case v1_8_R3:
                this.nmsVersion = VersionControl1_8_R3.getInstance();
                break;
            default:
                throw new UnSupportedVersion("The server is running with a version not supported for this plugin!");
        }
    }

    public NmsVersion getNmsVersion(){
        return this.nmsVersion;
    }

}
