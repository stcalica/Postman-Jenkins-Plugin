package io.jenkins.plugins.postman;

import java.util.ArrayList;
import java.util.List;


public class PostmanCollection {
  public String id;
  public String name;
  public String schema;
  public String uid;


  PostmanCollection(String postmanId, String name, String schema, String uid){
    this.id = postmanId;
    this.name = name;
    this.schema = schema;
    this.uid = uid;
  }

  public String getId(){
    return this.id;
  }

  public String getName(){
    return this.name;
  }

  public String getSchema(){
    return this.schema;
  }

  public String getUid(){
    return this.uid;
  }

  public String setId(String postmanId){
    return this.id = postmanId;
  }

  public String setName(String name){
    return this.name = name;
  }

  public String setSchema(String schema){
    return this.schema = schema;
  }

  public String setUid(String uid){
    return this.uid = uid;
  }
}
