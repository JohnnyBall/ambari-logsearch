/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ambari.logfeeder.output.cloud.upload;

import org.apache.ambari.logfeeder.conf.CloudStorageDestination;
import org.apache.ambari.logfeeder.conf.LogFeederProps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Factory class to create cloud specific data uploader client based on global Log Feeder settings.
 */
public class UploadClientFactory {

  private static final Logger logger = LogManager.getLogger(UploadClientFactory.class);

  /**
   * Create a new cloud specific client that can upload data to cloud storage
   * @param logFeederProps global settings
   * @return created cloud specific uploader client
   */
  public static UploadClient createUploadClient(LogFeederProps logFeederProps) {
    CloudStorageDestination destType = logFeederProps.getCloudStorageDestination();
    logger.info("Creating upload client for storage: {}", destType);
    if (CloudStorageDestination.HDFS.equals(destType)) {
      return new HDFSUploadClient(logFeederProps.getHdfsOutputConfig());
    } else if (CloudStorageDestination.S3.equals(destType)) {
      if (logFeederProps.isUseCloudHdfsClient()) {
        return new HDFSS3UploadClient(logFeederProps.getS3OutputConfig());
      } else {
        return new S3UploadClient(logFeederProps.getS3OutputConfig());
      }
    } else {
      throw new IllegalArgumentException(String.format("No cloud storage type is selected as destination: %s", destType));
    }
  }
}
