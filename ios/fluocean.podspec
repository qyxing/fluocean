#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint fluocean.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'fluocean'
  s.version          = '0.1.0'
  s.summary          = 'Oceanengine Pangolin Plugin'
  s.description      = <<-DESC
Oceanengine's Pangolin Plugin
                       DESC
  s.homepage         = 'https://github.com/qyxing/fluocean'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'qyxing.cn' => 'coreblock@gmail.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'
  s.platform = :ios, '9.0'

  s.dependency 'Bytedance-UnionAD'

  # Flutter.framework does not contain a i386 slice. Only x86_64 simulators are supported.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'VALID_ARCHS[sdk=iphonesimulator*]' => 'x86_64' }
  s.swift_version = '5.0'
end
