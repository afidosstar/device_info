import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:device_info/device_info.dart';

void main() {
  const MethodChannel channel = MethodChannel('device_info');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getAndroidID', () async {
    expect(await DeviceInfo.androidID, '42');
  });
  test('getBuildNumber', () async {
    expect(await DeviceInfo.buildNumber, '42');
  });
  test('getVersion', () async {

    expect(await DeviceInfo.version, '42');
  });
  test('getSerialNumber', () async {
    expect(await DeviceInfo.serialNumber, '42');
  });
}
