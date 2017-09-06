using System.IO;
using System.Text;

namespace EVTC_Log_Parser.Model
{
    public static class BinaryReaderExtensions
    {
        #region Extensions
        public static BinaryReader Skip(this BinaryReader reader, int length)
        {
            reader.BaseStream.Seek(length, SeekOrigin.Current);
            return reader;
        }

        public static string ReadUTF8(this BinaryReader reader, int length)
        {
            return Encoding.UTF8.GetString(reader.ReadBytes(length)).TrimEnd('\0');
        }

        public static string ReadUInt64Hex(this BinaryReader reader)
        {
            return string.Format("{0:x}", reader.ReadUInt64());
        }
        #endregion
    }
}
