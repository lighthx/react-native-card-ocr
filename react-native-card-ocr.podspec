Pod::Spec.new do |s|
  s.name         = "react-native-card-ocr"
  s.version      = "1.0.0"
  s.summary      = "react-native-card-ocr"

  s.description  = "react-native-card-ocr"

  s.homepage     = "https://github.com/lovebing/react-native-baidu-map"
  s.screenshots  = "https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/android.jpg", "https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/ios.jpg"

  s.license      = "MIT"
  s.author             = { "xin" => "lighthx@gmail.com" }

  s.platform     = :ios, "9.0"
  s.source       =  { :git => "http://git.xyd.cn/huaxin/react-native-card-ocr.git", :tag => "v#{s.version}" }
  s.source_files  = "ios/**/*.{m,h,mm}"
  s.frameworks = "CoreLocation", "QuartzCore", "OpenGLES", "SystemConfiguration", "CoreGraphics", "Security", "CoreTelephony"
  s.resources='ios/JYBD_IDCardRecognition/dicts/*','ios/JYBD_IDCardRecognition/Resources/*'
  s.vendored_libraries='ios/JYBD_IDCardRecognition/Classes/exbankcardcore/libbexbankcard.a','ios/JYBD_IDCardRecognition/Classes/exbankcardcore/libexbankcardios.a'
  s.dependency "React"
end
