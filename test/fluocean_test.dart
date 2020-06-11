import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:fluocean/fluocean.dart';

void main() {
  const MethodChannel channel = MethodChannel('fluocean');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Fluocean.platformVersion, '42');
  });
}
