
import 'dart:async';

import 'package:flutter/services.dart';

class DeviceInfo {
  static const MethodChannel _channel = MethodChannel('device_info');

  static Future<String?> get androidID async {
    final String? version = await _channel.invokeMethod('getAndroidID');
    return version;
  }

  static Future<String?> get serialNumber async {
    final String? response = await _channel.invokeMethod('getSerialNumber');
    return response;
  }
  static Future<String?> get version async {
    final String? response = await _channel.invokeMethod('getVersion');
    return response;
  }

  static Future<String?> get buildNumber async {
    final String? response = await _channel.invokeMethod('getBuildNumber');
    return response;
  }
}
