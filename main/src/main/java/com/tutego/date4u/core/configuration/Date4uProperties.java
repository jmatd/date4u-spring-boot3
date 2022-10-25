package com.tutego.date4u.core.configuration;

import com.tutego.date4u.core.FileSystem;
import com.tutego.date4u.core.photo.Photo;
import com.tutego.date4u.core.photo.PhotoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("date4u")
public class Date4uProperties {


    @Component
    public class OnStartUp implements CommandLineRunner {


        private final FileSystem fileSystem;

        public OnStartUp(FileSystem fileSystem) {
            this.fileSystem = fileSystem;
        }

        @Override
        public void run(String... args) throws Exception {
//            if(fileSystem.load("dummyPhoto") == null){
//                fileSystem.store("dummyPhoto.jpg", );
//            }
        }

    }

    public static class Filesystem {
        /**
         * Required minimum free disk space for local file system.
         */
        private long minimumFreeDiskSpace = 1_000_000;

        public long getMinimumFreeDiskSpace() {
            return minimumFreeDiskSpace;
        }

        public void setMinimumFreeDiskSpace(long minimumFreeDiskSpace) {
            this.minimumFreeDiskSpace = minimumFreeDiskSpace;
        }
    }

    private Filesystem filesystem = new Filesystem();

    public Filesystem getFilesystem() {
        return filesystem;
    }

    public void setFilesystem(Filesystem filesystem) {
        this.filesystem = filesystem;
    }
}