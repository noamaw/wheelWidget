import { NativeModules } from 'react-native';

const { SpinWheelModule } = NativeModules;

export function openWheel() {
  SpinWheelModule.openWheel();
}
