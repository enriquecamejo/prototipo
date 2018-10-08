<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output indent="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:param name="text"/>

  <xsl:mode on-no-match="shallow-copy"/>

  <xsl:template name="init">
    <xsl:apply-templates select="xml-to-json($text)"/>
  </xsl:template>

</xsl:stylesheet>