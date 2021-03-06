package is.hail.expr.types.physical

import is.hail.annotations.{Region, UnsafeOrdering}
import is.hail.asm4s.{Code, MethodBuilder}

abstract class ComplexPType extends PType {
  val representation: PType

  override def byteSize: Long = representation.byteSize

  override def alignment: Long = representation.alignment

  override def unsafeOrdering(): UnsafeOrdering = representation.unsafeOrdering()

  override def fundamentalType: PType = representation.fundamentalType

  override def containsPointers: Boolean = representation.containsPointers

  def copyFromType(mb: MethodBuilder, region: Code[Region], srcPType: PType, srcAddress: Code[Long], forceDeep: Boolean): Code[Long] = {
    assert(this isOfType srcPType)

    val srcRepPType = srcPType.asInstanceOf[ComplexPType].representation

    this.representation.copyFromType(mb, region, srcRepPType, srcAddress, forceDeep)
  }

  def copyFromTypeAndStackValue(mb: MethodBuilder, region: Code[Region], srcPType: PType, stackValue: Code[_], forceDeep: Boolean): Code[_] =
    this.representation.copyFromTypeAndStackValue(mb, region, srcPType.asInstanceOf[ComplexPType].representation, stackValue, forceDeep)

  def copyFromType(region: Region, srcPType: PType, srcAddress: Long, forceDeep: Boolean): Long = {
    assert(this isOfType srcPType)

    val srcRepPType = srcPType.asInstanceOf[ComplexPType].representation

    this.representation.copyFromType(region, srcRepPType, srcAddress, forceDeep)
  }

  def constructAtAddress(mb: MethodBuilder, addr: Code[Long], region: Code[Region], srcPType: PType, srcAddress: Code[Long], forceDeep: Boolean): Code[Unit] =
    this.representation.constructAtAddress(mb, addr, region, srcPType.fundamentalType, srcAddress, forceDeep)

  def constructAtAddress(addr: Long, region: Region, srcPType: PType, srcAddress: Long, forceDeep: Boolean): Unit =
    this.representation.constructAtAddress(addr, region, srcPType.fundamentalType, srcAddress, forceDeep)

  override def constructAtAddressFromValue(mb: MethodBuilder, addr: Code[Long], region: Code[Region], srcPType: PType, src: Code[_], forceDeep: Boolean): Code[Unit] =
    this.representation.constructAtAddressFromValue(mb, addr, region, srcPType.fundamentalType, src, forceDeep)
}