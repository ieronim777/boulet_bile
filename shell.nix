{ pkgs ? import ./pkgs.nix {} }: with pkgs;

stdenvNoCC.mkDerivation {
  name = "prism-break";
  nativeBuildInputs = [ clojure brotli zopfli ];
}
