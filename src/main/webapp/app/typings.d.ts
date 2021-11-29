declare const VERSION: string;
declare const SERVER_API_URL: string;
declare const DEVELOPMENT: string;

declare module '*.json' {
  const value: any;
  export default value;
}
