package com.wilky.dummysitecontroller.model;

import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("stable.dwk")
@Version("v1")
@Kind("DummySite")
@Plural("dummysites")
public class DummySite extends CustomResource<DummySiteSpec, Void> {
}
