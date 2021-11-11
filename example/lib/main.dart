import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:device_info/device_info.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _serialNumber = 'Unknown';
  String _androidID = 'Unknown';
  String _buildNumber = 'Unknown';
  String _version = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String serialNumber;
    String androidID;
    String buildNumber;
    String version;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      serialNumber =
          await DeviceInfo.serialNumber ?? 'Unknown platform  serial number';
    } on PlatformException {
      serialNumber = 'Failed to get serial number version.';
    }
    try {
      androidID =
          await DeviceInfo.androidID ?? 'Unknown platform  android ID';
    } on PlatformException {
      androidID = 'Failed to get android ID version.';
    }
    try {
      buildNumber =
          await DeviceInfo.buildNumber ?? 'Unknown platform  build number';
    } on PlatformException {
      buildNumber = 'Failed to get build number version.';
    }

    try {
      version =
          await DeviceInfo.version ?? 'Unknown platform  version';
    } on PlatformException {
      version = 'Failed to get version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _serialNumber = serialNumber;
      _androidID = androidID;
      _version = version;
      _buildNumber = buildNumber;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(children: [
            Text('Serial number: $_serialNumber\n'),
            Text('Version: $_version\n'),
            Text('Build number: $_buildNumber\n'),
            Text('android ID: $_androidID\n')
          ],),
        ),
      ),
    );
  }
}
