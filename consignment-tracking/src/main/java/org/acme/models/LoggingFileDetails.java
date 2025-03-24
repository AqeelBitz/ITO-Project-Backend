package org.acme.models;

import java.sql.Time;
import java.util.Date;

public class LoggingFileDetails {
    public int file_id;
    public String file_name;
    public int record_count;
    public int accept_record_count;
    public int reject_record_count;
    public int user_id;
    public Date file_date;
    public Time file_time;

    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getRecord_count() {
        return record_count;
    }

    public void setRecord_count(int record_count) {
        this.record_count = record_count;
    }

    public int getAccept_record_count() {
        return accept_record_count;
    }

    public void setAccept_record_count(int accept_record_count) {
        this.accept_record_count = accept_record_count;
    }

    public int getReject_record_count() {
        return reject_record_count;
    }

    public void setReject_record_count(int reject_record_count) {
        this.reject_record_count = reject_record_count;
    }

    public Date getFileDate() {
        return file_date;
    }

    public void setFileDate(Date date) {
        this.file_date = date;
    }

    public Time getFileTime() {
        return file_time;
    }

    public void setFileTime(Time file_time) {
        this.file_time = file_time;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "LoggingFileDetails{" +
                "fileId=" + file_id +
                ", fileName='" + file_name + '\'' +
                ", recordCount=" + record_count +
                ", acceptRecordCount=" + accept_record_count +
                ", rejectRecordCount=" + reject_record_count +
                ", userId=" + user_id +
                ", fileDate=" + file_date +
                ", fileTime=" + file_time +
                '}';
    }

  
}
