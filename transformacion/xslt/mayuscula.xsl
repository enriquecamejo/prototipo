<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xhtml="http://www.w3.org/1999/xhtml">
    <xsl:output method="xml" encoding="UTF-8"/>
    
    <xsl:variable name="smallCase" select="'abcdefghijklmnopqrstuvwxyz'"/>
    <xsl:variable name="upperCase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
    
    <xsl:template match="@*|node()">
	    <xsl:copy>
	        <xsl:apply-templates select="@*|node()"/>
	    </xsl:copy>
    </xsl:template>

    <xsl:template match="node()[not(@lang)]/text()">
        <xsl:value-of select="translate(., $smallCase, $upperCase)"/>
    </xsl:template>
</xsl:stylesheet>